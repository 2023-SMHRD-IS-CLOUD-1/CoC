import io
import torch
import base64
from PIL import Image
import json
import flask
from flask import Flask, request, jsonify
import requests

import clip
import torch

app = Flask(__name__) ## 앱을 생성한다.

## 모델 로딩 (필수)
device = "cuda" if torch.cuda.is_available() else "cpu"
model, preprocess = clip.load('ViT-B/32', device)


# 이미지를 예측하고 결과를 출력하는 함수 (이미지 출력 추가)
def predict_image(image, tclasses):
    # 이미지 전처리
    image_input = preprocess(image).unsqueeze(0).to(device)

    # 텍스트 입력 생성
    text_inputs = torch.cat([clip.tokenize(f"a photo of a {c}") for c in tclasses]).to(device)

    # 이미지와 텍스트의 특징 계산
    with torch.no_grad():
        image_features = model.encode_image(image_input)
        text_features = model.encode_text(text_inputs)

    # 이미지와 텍스트 간의 유사도 계산
    image_features /= image_features.norm(dim=-1, keepdim=True)
    text_features /= text_features.norm(dim=-1, keepdim=True)
    similarity = (100.0 * image_features @ text_features.T).softmax(dim=-1)
    values, indices = similarity[0].topk(9)

    values = values.detach().cpu().numpy().tolist()
    indices = indices.detach().cpu().numpy().tolist()

    return {
        "values": values,
        "indices": indices,
    }

@app.route('/predict', methods=['POST'])
def predict():
        data = request.json
        image_links = data['image']  ## 이미지링크 리스트
        query = data['query']  ## 텍스트 쿼리
        
        # 각 이미지에 대한 예측 결과를 저장할 배열 초기화
        results = []

        # 각 이미지에 대해 예측 실행
        response = requests.get(image_links.strip())
            
        # 이미지를 읽어올 수 있는지 확인
        if response.status_code == 200:
            # 이미지가 성공적으로 읽어와졌을 때 예측 실행
            image = Image.open(io.BytesIO(response.content))
            predictions = predict_image(image, query)

            index = predictions['indices'][0]
            prob = predictions['values'][0]

                # 이미지링크와 예측된 태그를 배열에 추가
            results.append({
                "image_link": image_links,
                "tag": query[index],
                "probability": prob
            })
        else:
            # 이미지를 읽어올 수 없는 경우에 대한 처리
            print('read fail!!!!')
            results.append({
                "image_link": image_links,
                "error": "Failed to fetch image"
            })

        # 결과 반환
        return jsonify(results)
    
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4000, debug=True)