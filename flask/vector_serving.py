from flask import Flask, request, jsonify
import requests
import numpy as np
from PIL import Image
import io
import torch
import torchvision.models as models
import torchvision.transforms as transforms
import os

app = Flask(__name__)

# 이미지 처리를 위한 변환 정의
preprocess = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
])

# ResNet50 모델 불러오기
model = models.resnet50(pretrained=True)
model.eval()

@app.route('/predict', methods=['POST'])
def predict():
    data = request.json    # 클라이언트로부터 이미지 링크 받기
    features_list = []

    for image_link in data:
        print(image_link)
        url = image_link
        url = url.replace('"','')

        response = requests.get(url)
        image = Image.open(io.BytesIO(response.content))

        # 이미지 전처리 및 특징 벡터 추출
        input_tensor = preprocess(image)
        input_batch = input_tensor.unsqueeze(0)
        with torch.no_grad():
            features = model(input_batch)

        # 이미지 URL에서 파일 이름 추출
        filename = os.path.basename(url)

        # 파일 이름과 해당 이미지의 특징 벡터를 함께 반환
        result = {'filename': filename, 'features': features.tolist()}
        features_list.append(result)

    return jsonify(features_list)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4005)
