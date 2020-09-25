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
    return "<h1>TITLE</h1><p>BANK.</p>"

@app.route('/list', methods=['GET'])
def list_all():
    banks = db.list_all_banks()
    return json.dumps(banks, default=str)

@app.route('/find.name', methods=['GET'])
def find_by_name():
    name = request.args.get('name')
    banks = db.find_bank_by_name(name)
    return json.dumps(banks, default=str)


@app.route('/find.key', methods=['GET'])
def find_by_key():
    key = request.args.get('key')
    banks = db.find_bank_by_key(key)
    return json.dumps(banks, default=str)


app.run(host='127.0.0.1', port=8070)
