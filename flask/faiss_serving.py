import torchvision.models as models
from torchvision import transforms
from PIL import Image
import os
import torch

import io
import base64
import json
import flask
from flask import Flask, request, jsonify

import faiss
import numpy as np

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

# index 로드
index = faiss.read_index('my_index.index')

# filenameList 로드
filename_list = []
with open('./filename_list.text', "r", encoding='cp949') as file:
    for i in file:
        filename_list.append(i.strip())

app = Flask(__name__) ## 앱을 생성한다.
@app.route('/predict', methods=['POST'])
def predict():
    if request.method == 'POST':
        data = flask.request.data.decode('utf-8')
        s = io.StringIO(data)
        js = json.load(s) ## client의 body
        
        image = Image.open(io.BytesIO(base64.b64decode(js['image'].split(',')[-1]))).convert('RGB')
        # 이미지 전처리
        input_tensor = preprocess(image)

        # 배치 차원 추가 ([N, C, H, W] 형식으로 만들기)
        input_batch = input_tensor.unsqueeze(0)

        # GPU 사용 가능 여부 확인 후 사용
        if torch.cuda.is_available():
            input_batch = input_batch.to('cuda')
            model.to('cuda')

        with torch.no_grad():
            # 모델에 이미지 전달 및 특징 벡터 추출
            features = model(input_batch)

        # features 텐서는 [1, 2048, 1, 1] 형태를 가짐
        # 일반적으로는 [1, 2048] 형태로 사용하기 위해 텐서 조정
        features = torch.flatten(features)
        img_vector = features.cpu().numpy()

        ## 쿼리 벡터 준비 (입력하는 이미지 벡터)
        ## 내가 가지고 있는 0번째 벡터로 입력 해보기 (테스트)
        xq = np.array([img_vector])

        ## 벡터 검색
        k = 10  # 찾고자 하는 이미지 벡터 개수
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
    app.run(host='0.0.0.0', port=4001, debug=True)