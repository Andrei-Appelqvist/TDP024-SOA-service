import flask

app = flask.Flask(__name__)
app.config["DEBUG"] = True

@app.route('/', methods=['GET'])

def home():
    return "<h1>TITLE</h1><p>Paragraph.</p>"

#app.run(host='127.0.0.1', port=8070)
