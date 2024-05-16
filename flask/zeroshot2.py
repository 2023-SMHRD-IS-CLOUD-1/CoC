import os
import torch
from torchvision import transforms
from torchvision.transforms.functional import InterpolationMode
from flask import Flask, request, jsonify
from groundingdino.util.inference import load_model, load_image2, predict
import requests
from io import BytesIO
from PIL import Image

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

app = Flask(__name__)

CONFIG_PATH = os.path.join("./", "./groundingdino/config/GroundingDINO_SwinT_OGC.py")
WEIGHTS_NAME = "groundingdino_swint_ogc.pth"
WEIGHTS_PATH = os.path.join("./", "weights", WEIGHTS_NAME)

model = load_model(CONFIG_PATH, WEIGHTS_PATH)

@app.route('/predictt3', methods=['POST'])
def zeroshot():
    # 클라이언트로부터 이미지 파일 경로를 받음
    data = request.json
    # IMAGE_PATH = data.get("image_path", "")
    result=[]
    
    TEXT_PROMPT = "building, clothing, vehicle, sports, food, plant, animal, person, document, furniture"
    BOX_THRESHOLD = 0.35
    TEXT_THRESHOLD = 0.20
    
    tagList = ["building", "clothing", "vehicle", "sports",
       "food", "plant", "animal", "person", "document", "furniture"]
    for i in data:
        url = i
        url = url.replace('"','')
        response = requests.get(url)
        image_source, image = load_image2(BytesIO(response.content))
        # image = Image.open(BytesIO(response.content))
        # image_source, image = load_image(IMAGE_PATH)

        boxes, logits, phrases = predict(
            model=model,
            image=image,
            caption=TEXT_PROMPT,
            box_threshold=BOX_THRESHOLD,
            text_threshold=TEXT_THRESHOLD
        )

        tag = []
        for i, (box, phrase) in enumerate(zip(boxes, phrases)):
            tag.append(phrase)
            print()
        yesNoList =["no", "no", "no", "no", "no", "no", "no", "no", "no", "no"]
        for i in tag:
            for c, j in enumerate(tagList):
                if i == j:
                    yesNoList[c]="yes"
                    break
        result.append(yesNoList)
    result.append(tagList)
    print(result)
    return jsonify({'tagList': result}), 200

@app.route('/predictt4', methods=['POST'])
def zeroshotPlus():
    # 클라이언트로부터 이미지 파일 경로를 받음
    data = request.json
    # IMAGE_PATH = data.get("image_path", "")
    result=[]
    
    
    for i in data:
        TEXT_PROMPT = "building, clothing, vehicle, sports, food, plant, animal, person, document, furniture"
        BOX_THRESHOLD = 0.35
        TEXT_THRESHOLD = 0.20
        
        tagList1 = ["building", "clothing", "vehicle", "sports",
       "food", "plant", "animal", "person", "document", "furniture"]
        
        url = i
        url = url.replace('"','')
        response = requests.get(url)
        image_source, image = load_image2(BytesIO(response.content))
        # image = Image.open(BytesIO(response.content))
        # image_source, image = load_image(IMAGE_PATH)

        boxes, logits, phrases = predict(
            model=model,
            image=image,
            caption=TEXT_PROMPT,
            box_threshold=BOX_THRESHOLD,
            text_threshold=TEXT_THRESHOLD
        )

        tag = []
        for i, (box, phrase) in enumerate(zip(boxes, phrases)):
            print(f"[ Confidence: {logits[i].item()}, Object Name: {phrase}]")
            tag.append(phrase)
        yesNoList1 =["no", "no", "no", "no", "no", "no", "no", "no", "no", "no"]
        for i in tag:
            for c, j in enumerate(tagList1):
                if i == j:
                    yesNoList1[c]="yes"
                    break
                
        TEXT_PROMPT = "Dog, cat, sea, sky, night view, dessert, drink, art, beauty, books"
        tagList2 = ["Dog", "cat", "sea", "sky", "night view", "dessert", "drink", "art", "beauty", "books"]
        
        boxes, logits, phrases = predict(
            model=model,
            image=image,
            caption=TEXT_PROMPT,
            box_threshold=BOX_THRESHOLD,
            text_threshold=TEXT_THRESHOLD
        )
        
        tag = []
        for i, (box, phrase) in enumerate(zip(boxes, phrases)):
            tag.append(phrase)
        yesNoList2 =["no", "no", "no", "no", "no", "no", "no", "no", "no", "no"]
        for i in tag:
            for c, j in enumerate(tagList2):
                if i == j:
                    yesNoList2[c]="yes"
                    break
        yesNoList=yesNoList1+yesNoList2
        print("yesNoList:", yesNoList)
        result.append(yesNoList)
    result.append(tagList1+tagList2)
    print(result)
    return jsonify({'tagList': result}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4010, debug=True)