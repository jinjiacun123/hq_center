import httplib2
import ConfigParser

def http_down(url, filename):
    h = httplib2.Http()
    resp, content = h.request(url)

    if resp['status'] == '200':
        with open(filename, 'wb') as f:
            f.write(content)
        return True
    print resp['status']
    return False

def unzip_right(filename):
    import zipfile, os
    exampleZip = zipfile.ZipFile(filename)
    exampleZip.extractall()
    exampleZip.close()

class Db_Connector:
  def __init__(self, config_file_path):
    cf = ConfigParser.ConfigParser()
    cf.read(config_file_path)
    s = cf.sections()
    print 'section:', s
    o = cf.options("baseconf")
    print 'options:', o
    v = cf.items("baseconf")
    print 'db:', v
    db_host = cf.get("baseconf", "host")
    db_port = cf.getint("baseconf", "port")
    db_user = cf.get("baseconf", "user")
    db_pwd = cf.get("baseconf", "password")
    print db_host, db_port, db_user, db_pwd
    cf.set("baseconf", "db_pass", "123456")
    cf.write(open("config_file_path", "w"))

def read_ini(base, name):
    config_file_path = './config.ini'
    cf = ConfigParser.ConfigParser()
    cf.read(config_file_path)
    return cf.get(base, name)

