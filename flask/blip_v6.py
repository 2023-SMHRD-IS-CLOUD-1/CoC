from PIL import Image
import torch
from torchvision import transforms
from torchvision.transforms.functional import InterpolationMode
from models.blip_vqa import blip_vqa
import requests
from io import BytesIO

from flask import Flask, request, jsonify


device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

# 모델 세팅
image_size = 480
model = blip_vqa(pretrained='model_base_vqa_capfilt_large.pth', image_size=image_size, vit='base')
model.eval()
model = model.to(device)

tag = ["building", "clothing", "vehicle", "sports",
       "food", "plant", "animal", "person", "document"]
question = []
for i in tag:
    question.append('is there a '+i)

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



app = Flask(__name__)
data=[]
@app.route('/predictt', methods=['POST'])
def receive_array():
    result = []
    data = request.json  # 클라이언트에서 보낸 JSON 데이터 받기
    for i in data:
        print(i)
        url = i
        url = url.replace('"','')
        response = requests.get(url)
        image = Image.open(BytesIO(response.content))
        tagList=[]
        for q in question:
            text= q
            answer = find_obj(image, text)
            tagList.append(answer)
        print('태그출력', tagList)
        result.append(tagList)
    result.append(tag)
    print("리절트출력",result)
    return jsonify({'tagList': result}), 200



@app.route('/predictt2', methods=['POST'])
def receive_array2():
    result = []
    # print()
    data = request.json  # 클라이언트에서 보낸 JSON 데이터 받기
    for i in data[0]:
        # print('여기',i)
        url = i
        url = url.replace('"','')
        response = requests.get(url)
        image = Image.open(BytesIO(response.content))
        tagList=[]
        for i in data[1]:
            text = i#.replace('"','')
            answer = find_obj(image, ('is there a '+text))
            # print('태그 테스트', ('is there a '+text))
            tagList.append(answer)
        print('태그출력', tagList)
        result.append(tagList)
    print('위치00')
    result.append(data[1])
    print('위치01')
    print("리절트출력",result)
    print('위치02')
    return jsonify({'tagList': result}), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4000, debug=True)
    
# s3에 저장된 이미지를 url받은 개수만큼 모델을 돌림



