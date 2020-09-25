import db_api
import person_api
import bank_api
from flask import Flask, request

app = Flask(__name__)

db = db_api.DBM()
db.connect()

app.register_blueprint(person_api.person_api_constructor(db), url_prefix="/person")
