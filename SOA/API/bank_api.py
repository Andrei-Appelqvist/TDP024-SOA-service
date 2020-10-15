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

@app.route('/bank/list', methods=['GET'])
def list_all():
    banks = db.list_all_banks()
    return json.dumps(banks, default=str)

@app.route('/bank/find.name', methods=['GET'])
def find_by_name():
    name = request.args.get('name')
    banks = db.find_bank_by_name(name)
    if len(banks) > 0:
        return json.dumps(banks[0], default=str)
    return 'null'



@app.route('/bank/find.key', methods=['GET'])
def find_by_key():
    key = request.args.get('key')
    if(len(key) > 0):
        banks = db.find_bank_by_key(key)
        if len(banks) > 0:
            return json.dumps(banks[0], default=str)
        return 'null'
    else:
        return 'null'


app.run(host='127.0.0.1', port=8070)
