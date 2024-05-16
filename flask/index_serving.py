import boto3
import json
import faiss
import numpy as np
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

@app.route('/update', methods=['PUT'])
def update_model():
    data = request.data.decode('utf-8')
    js = json.loads(data)
    print("시작!!!")

    # 유저 id 입력받음
    user_num = js['user_num']
    print("user_num : ",user_num)

    # s3_resource = boto3.resource('s3')
    s3_client = boto3.client('s3')
    bucket_name = 'codewi'  # S3 버킷 이름
    prefix = f'user_num{user_num}/vector'  # user vector가 저장되어 있는 폴더
    
    # bucket = s3_resource.Bucket(bucket_name)
    # objects = bucket.objects.filter(Prefix=prefix)
    
    paginator = s3_client.get_paginator('list_objects_v2')
    
    
    filename_list = []  # 이미지 파일명 목록
    vector_list = []  # 벡터 목록
    # Paginator를 사용해 특정 폴더(접두사)에 있는 객체들을 페이지별로 처리
    for page in paginator.paginate(Bucket=bucket_name, Prefix=prefix):
        # 페이지 내 객체(Contents)를 반복 처리
        for content in page.get('Contents', []):
            # 객체 키 가져오기
            object_key = content['Key']
            print("object_key : ",object_key)
            # 객체 키가 '.json'으로 끝나는지 확인
            if object_key.endswith('.json'):
                # JSON 파일이므로 처리 로직 추가
                response = s3_client.get_object(Bucket=bucket_name, Key=object_key)
                json_content = response['Body'].read().decode('utf-8')
                filename_list.append(object_key.split('/')[-1][:-5] + '.jpg')
                vector_list.append(json.loads(json_content))

    print("파일네임리스트 : ",filename_list)
    print("추가끝!!!")
    # numpy 배열로 변환
    vector_np = np.array(vector_list)
    print("변환 완료")

    # 벡터를 사용하여 새로운 인덱스 생성
    d = len(vector_list[0])  # 벡터 차원
    print("d : ", d)
    index = faiss.IndexFlatL2(d)
    index.add(vector_np)
    print("인덱스 : ",index)

    # 인덱스와 파일네임 리스트를 로컬에 저장
    faiss.write_index(index, 'my_index.index')
    with open('filename_list.json', 'w') as json_f:
        json.dump(filename_list, json_f, indent='\t', ensure_ascii=False)

    # 인덱스와 파일네임 리스트를 S3에 저장
    s3_client = boto3.client('s3')
    index_path = f'user_num{user_num}/index/my_index.index'  # S3에 인덱스를 저장할 경로
    filename_list_path = f'user_num{user_num}/index/filename_list.json'  # S3에 파일네임 리스트를 저장할 경로
    s3_client.upload_file('my_index.index', bucket_name, index_path)
    s3_client.upload_file('filename_list.json', bucket_name, filename_list_path)

    return jsonify({'message': 'Model weights updated successfully'}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4001, debug=True)
