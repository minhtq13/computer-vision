from flask import Flask, jsonify, request
import os
from scoring import score
from scoring_accuracy import score as score_accuracy

app = Flask(__name__)

SERVER_API_KEY = "apikeyels2019271820232"


@app.route('/api/scoring', methods=['POST'])
def scoring():
    api_key = request.headers.get('apiKey')
    request_body_json = request.get_json()
    exam_class_code = request_body_json.get('examClassCode')
    mode = request_body_json.get('mode')
    selected_images = request_body_json.get('selectedImages')
    number_answer = request_body_json.get('numberAnswers')
    try:
        if api_key is None or api_key != os.environ.get('SERVER_API_KEY', SERVER_API_KEY):
            raise Exception('Access Denied')
        if exam_class_code is None:
            raise Exception("Invalid examClassCode")
        if mode == 'accuracy':
            score_accuracy(exam_class_code=exam_class_code, selected_images=selected_images, number_answer=number_answer)
        else:
            score(exam_class_code=exam_class_code, selected_images=selected_images, number_answer=number_answer)
        return jsonify({"examClassCode": exam_class_code, "status": "successfully", "message": "Scoring successfully!"})
    except Exception as ex:
        return jsonify({"examClassCode": exam_class_code, "status": "error", "message": str(ex)})


# Server run
if __name__ == '__main__':
    port = int(os.environ.get('SERVER_PORT', 8089))
    run_env = os.environ.get('RUN_ENV', 'local')
    app.run(host='0.0.0.0', port=port, debug=False if run_env is None and run_env == 'production' else True)
