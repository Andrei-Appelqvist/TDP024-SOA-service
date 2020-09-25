#import flask
import db_api
from flask import Blueprint, request, jsonify

def person_api_constructor(db):
    person_api = Blueprint("person_api", __name__)

    @person_api.route("/hello", methods=["GET"])
    def hello_world():
        print(db.prs())
        return "<h1>TITLE</h1><p>Paragraph.</p>"

    return person_api



"""
app = flask.Flask(__name__)
app.config["DEBUG"] = True
@app.route('/', methods=['GET'])

def home():
    db.connect()
    return "<h1>TITLE</h1><p>Paragraph.</p>"

#app.run(host='127.0.0.1', port=8060)
"""
