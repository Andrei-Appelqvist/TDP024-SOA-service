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

@app.route('/person/list', methods=['GET'])
def list_all():
    people = db.list_all_persons()
    print(people)
    return json.dumps(people, default=str)

@app.route('/person/find.name', methods=['GET'])
def find_by_name():
    name = request.args.get('name')
    people = db.find_person_by_name(name)
    return json.dumps(people, default=str)


@app.route('/person/find.key', methods=['GET'])
def find_by_key():
    key = request.args.get('key')
    people = db.find_person_by_key(key)
    if len(people) > 0:
        return json.dumps(people, default=str)
    return 'null'


app.run(host='127.0.0.1', port=8060)
