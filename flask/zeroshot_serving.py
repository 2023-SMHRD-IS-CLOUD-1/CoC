from flask import Flask, request, jsonify
from groundingdino.util.inference import load_model,predict, load_image
from PIL import Image
import base64
import io
import torch

app = Flask(__name__)

# Load the model
model = load_model("GroundingDINO/groundingdino/config/GroundingDINO_SwinT_OGC.py", "GroundingDINO/weights/groundingdino_swint_ogc.pth")
model = model.eval()

@app.route('/predict', methods=['POST'])
def predict_objects():
    # Get data from the request
    data = request.json
    base64_str = data['image']
    query = data['query']
    
    # Decode the base64 image
    image_bytes = base64.b64decode(base64_str)
    image = Image.open(io.BytesIO(image_bytes))
    image.save('tmp.jpg')
    _, image = load_image('tmp.jpg')
    
    # Perform object detection
    with torch.no_grad():
        boxes, logits, phrases = predict(
            model=model,
            image=image,
            caption=query,
            box_threshold=0.35,
            text_threshold=0.25,
            device='cpu'  
        )
    
    for i, (box, phrase) in enumerate(zip(boxes, phrases)):
        print(f"Detection {i + 1}: [Confidence: {logits[i].item()}, Object Name: {phrase}]")

    # Format the response
    response = {
        #'boxes': [box.tolist() for box in boxes], # 박스위치
        'logits': [logit.item() for logit in logits], # 신뢰도
        'phrases': phrases  # 태그
    }
    
    return jsonify(response)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4003, debug=True)