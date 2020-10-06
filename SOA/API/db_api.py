import pymongo, bson
from flask import jsonify
import json

class DBM:

    def __init__(self):
        pass

    def connect(self):
        self.client = pymongo.MongoClient("mongodb+srv://TDP-CREW:TDP024-SOA@cluster0.4aonh.mongodb.net/<dbname>?retryWrites=true&w=majority")
        self.db = self.client["bank-person-api"]
        self.persons = self.db["person"]
        self.banks = self.db["bank"]
        print("asdf")

    def prs(self):
        asdf = self.persons.find({})
        return "asdf"

    def list_all_persons(self):
        persons = self.persons.find()
        return list(persons)


    def find_person_by_name(self, pname):
        persons = self.persons.find({"name": pname})
        return list(persons)

    def find_person_by_key(self, pkey):
        person = self.persons.find({ "key": int(pkey)})
        return list(person)


    def list_all_banks(self):
        banks = self.banks.find()
        return list(banks)


    def find_bank_by_name(self, pname):
        banks = self.banks.find({"name": pname})
        return list(banks)

    def find_bank_by_key(self, pkey):
        banks = self.banks.find({ "key": int(pkey)})
        return list(banks)
