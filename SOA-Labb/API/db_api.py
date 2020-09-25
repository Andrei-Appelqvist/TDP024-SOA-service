import pymongo, bson
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
