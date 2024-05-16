import os
import io
import base64
import json
import boto3
import faiss
import numpy as np
from torchvision import transforms
from PIL import Image
import torch
import torchvision.models as models
import flask
from flask import Flask, request, jsonify
from flask_cors import CORS

# 모델 로드
model = models.resnet50(pretrained=True)

model.eval()

model = torch.nn.Sequential(*(list(model.children())[:-1]))

preprocess = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
])

app = Flask(__name__)
CORS(app)
@app.route('/predict', methods=['POST'])
def predict():
    if request.method == 'POST':
        
        data = flask.request.data.decode('utf-8')
        s = io.StringIO(data)
        js = json.load(s)
        
        image = Image.open(io.BytesIO(base64.b64decode(js['image_data'].split(',')[-1]))).convert('RGB')
        input_tensor = preprocess(image) # 이미지 전처리
        input_batch = input_tensor.unsqueeze(0) # 배치차원 추가
        user_num = js['user_num']
        print("user_num : ",user_num)

        # S3 클라이언트 생성
        s3_client = boto3.client('s3')
        bucket_name = 'codewi'
        index_file_key1 = f'user_num{user_num}/index/my_index.index'
        index_file_key2 = f'user_num{user_num}/index/filename_list.json'
        print("index_file_key1 : ",index_file_key1)
        print("index_file_key2 : ",index_file_key2)

        # 로컬에 파일 다운로드할 위치
        local_index_dir = f'./index_files/user_num{user_num}'
        print("local_index_dir : ",local_index_dir)
        os.makedirs(local_index_dir, exist_ok=True)
        
        local_index_file = os.path.join(local_index_dir, 'my_index.index')
        # local_index_file = f'./index_files/user_num{user_num}/my_index.index'
        # S3에서 인덱스 파일 다운로드
        s3_client.download_file(bucket_name, index_file_key1, local_index_file)
        
        # index 로드
        index = faiss.read_index('my_index.index')

        # local_index_file2 = f'./index_files/user_num{user_num}/filename_list.json'
        local_index_file2 = os.path.join(local_index_dir, 'filename_list.json')
        s3_client.download_file(bucket_name, index_file_key2, local_index_file2)

        # filename_list 로드
        filename_list = []
        with open(local_index_file2, "r") as file:
            filename_list = json.load(file)

        # GPU 사용 가능 여부 확인 후 사용
        if torch.cuda.is_available():
            input_batch = input_batch.to('cuda')
            model.to('cuda')

        with torch.no_grad():
            # 모델에 이미지 전달 및 특징 벡터 추출
            features = model(input_batch)

        features = torch.flatten(features)
        print("features : ", features.shape)
        img_vector = features.cpu().numpy()
        print("img_vector : ", img_vector.shape)

        ## 쿼리 벡터 준비 (입력하는 이미지 벡터)
        xq = np.array([img_vector])
        print("shape : ",xq.shape)

        ## 벡터 검색
        k = 5  # 찾고자 하는 이미지 벡터 개수
        D, I = index.search(xq, k)

        print(I)  # 유사한 벡터 인덱스 (벡터 번호)
        print(D)  # 이웃까지의 거리
        
        file_name_list = []
        for idx, dist in zip(I[0], D[0]):
            file_name_list.append(filename_list[idx])

        result = {
            "file_name_list": file_name_list,
        }

        return flask.Response(
            response=json.dumps(result, ensure_ascii=False, indent='\t'),
            status=200,
            mimetype="application/json"
        )

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4007, debug=True)