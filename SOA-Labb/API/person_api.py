import flask
from flask import jsonify, request
import db_api
import json

app = flask.Flask(__name__)
app.config["DEBUG"] = True

db = db_api.DBM()
db.connect()

@app.route('/', methods=['GET'])
def home():
    return "<h1>TITLE</h1><p>PERSON.</p>"

@app.route('/list', methods=['GET'])
def list_all():
    people = db.list_all_persons()
    return json.dumps(people, default=str)

@app.route('/find.name', methods=['GET'])
def find_by_name():
    name = request.args.get('name')
    people = db.find_person_by_name(name)
    return json.dumps(people, default=str)


@app.route('/find.key', methods=['GET'])
def find_by_key():
    key = request.args.get('key')
    people = db.find_person_by_key(key)
    return json.dumps(people, default=str)


app.run(host='127.0.0.1', port=8060)
