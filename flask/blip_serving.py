# # 이미지 로드, 전처리
from PIL import Image
import torch
from io import BytesIO
from torchvision import transforms
from torchvision.transforms.functional import InterpolationMode
from models.blip_vqa import blip_vqa
import requests

# 통신용 import
import io
import base64
import json
import flask
from flask import Flask, request, jsonify


device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

# 모델 세팅
image_size = 480
local_model_path='model_base_vqa_capfilt_large.pth'
model = blip_vqa(pretrained=local_model_path, image_size=image_size, vit='base')
model.eval()
model = model.to(device)

def find_obj(raw_image, question):
    # 전처리
    w,h = raw_image.size
    transform = transforms.Compose([
        transforms.Resize((image_size,image_size),interpolation=InterpolationMode.BICUBIC),
        transforms.ToTensor(),
        transforms.Normalize((0.48145466, 0.4578275, 0.40821073), (0.26862954, 0.26130258, 0.27577711))
    ])
    image = transform(raw_image).unsqueeze(0).to(device)
    
    # 모델 입출력
    with torch.no_grad():
        answer = model(image, question, train=False, inference='generate')
        return answer[0]

# 통신
app = Flask(__name__)
@app.route('/predict', methods=['POST'])
def receive_array():
    data = request.json  # 클라이언트에서 보낸 JSON 데이터 받기
    print('결과값' ,request.json)
    results = []
    for url in data['images']:
        response = requests.get(url)
        image = Image.open(BytesIO(response.content))
        text = data['query']
        answer = find_obj(image, text)
        results.append({'image_url': url, 'answer': answer})
    return jsonify(results)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4004, debug=True)