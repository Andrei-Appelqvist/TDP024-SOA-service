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

def get_datetime():
    today = datetime.now()
    dt_string = today.strftime("[%Y-%m-%d %H:%M:%S]")
    return dt_string

app = flask.Flask(__name__)
app.config["DEBUG"] = True

db = db_api.DBM()
db.connect()

@app.route('/', methods=['GET'])
def home():
    return "<h1>TITLE</h1><p>BANK.</p>"

@app.route('/bank/list', methods=['GET'])
def list_all():
    producer.send('rest-topic', "{REST (python)(bank): Request made for list all banks.}")
    banks = db.list_all_banks()
    producer.send('rest-topic', "{REST (python)(bank): List all request served succesfully.}")
    return json.dumps(banks, default=str)

@app.route('/bank/find.name', methods=['GET'])
def find_by_name():
    producer.send('rest-topic', "{REST (python)(bank): Find bank by name request started.}")
    name = request.args.get('name')
    banks = db.find_bank_by_name(name)
    if len(banks) > 0:
        producer.send('rest-topic', "{REST (python)(bank): Find bank by name request served}")
        return json.dumps(banks[0], default=str)
    producer.send('rest-topic', "{REST (python)(bank): No results found for request}")
    return 'null'





@app.route('/bank/find.key', methods=['GET'])
def find_by_key():
    producer.send('rest-topic', "{REST (python)(bank): Find bank by key request started.}")
    key = request.args.get('key')
    if(len(key) > 0):
        banks = db.find_bank_by_key(key)
        if len(banks) > 0:
            producer.send('rest-topic', "{REST (python)(bank): Find bank by key succesfully served.}")
            return json.dumps(banks[0], default=str)
        producer.send('rest-topic', "{REST (python)(bank): Find bank by key gave no results.}")
        return 'null'
    else:
        producer.send('rest-topic', "{REST (python)(bank): Find bank by key failed. Key argument was wrong or missing}")
        return 'null'


app.run(host='127.0.0.1', port=8070)
