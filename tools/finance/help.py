import httplib2

def http_down():
    filename = "./sysFin.zip"
    h = httplib2.Http()      
    url = 'http://221.6.167.248:8082/sysFin' 
    resp, content = h.request(url)  
       
    if resp['status'] == '200':  
        with open(filename, 'wb') as f:  
            f.write(content)


def unzip():
    import zipfile, os
    exampleZip = zipfile.ZipFile('sysFin.zip')
    exampleZip.extractall()
    exampleZip.close()
    pass

if __name__ == "__main__":
    http_down()
    unzip()
