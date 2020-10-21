import flask
from flask import jsonify, request
import db_api
import json
from time import sleep
from json import dumps
from kafka import KafkaProducer
from datetime import datetime

producer = KafkaProducer(bootstrap_servers=['localhost:9092'],
                         value_serializer=lambda x:
                         dumps(get_datetime()+x).encode('utf-8'))

app = flask.Flask(__name__)
app.config["DEBUG"] = True

db = db_api.DBM()
db.connect()

@app.route('/', methods=['GET'])
def home():
    return "<h1>TITLE</h1><p>PERSON.</p>"

@app.route('/person/list', methods=['GET'])
def list_all():
    producer.send('rest-topic', "{REST (python)(person): Request made for list all persons.}")
    people = db.list_all_persons()
    print(people)
    producer.send('rest-topic', "{REST (python)(person): List all request served succesfully.}")
    return json.dumps(people, default=str)

@app.route('/person/find.name', methods=['GET'])
def find_by_name():
    producer.send('rest-topic', "{REST (python)(person): Find person by name request started.}")
    name = request.args.get('name')
    people = db.find_person_by_name(name)
    producer.send('rest-topic', "{REST (python)(person): Find person by name request served}")
    return json.dumps(people, default=str)


@app.route('/person/find.key', methods=['GET'])
def find_by_key():
    producer.send('rest-topic', "{REST (python)(person): Find person by key request started.}")
    key = request.args.get('key')
    if(len(key) > 0):
        people = db.find_person_by_key(key)
        if len(people) > 0:
            producer.send('rest-topic', "{REST (python)(person): Find person by key succesfully served.}")
            return json.dumps(people, default=str)
        producer.send('rest-topic', "{REST (python)(person): Find person by key gave no results.}")
        return 'null'
    else:
        producer.send('rest-topic', "{REST (python)(person): Find person by key failed. Key argument was wrong or missing}")
        return 'null'

def get_datetime():
    today = datetime.now()
    dt_string = today.strftime("[%Y-%m-%d %H:%M:%S]")
    return dt_string


app.run(host='127.0.0.1', port=8060)
