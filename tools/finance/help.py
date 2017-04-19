import httplib2


#down right and finance binary data from network
def http_down_right(url, filename):    
    h = httplib2.Http()    
    resp, content = h.request(url)  
       
    if resp['status'] == '200':  
        with open(filename, 'wb') as f:  
            f.write(content)

#down dbf format data of finance from network
def http_down_finance(url, filename):
	h = httplib2.Http()    
    resp, content = h.request(url)  
       
    if resp['status'] == '200':  
        with open(filename, 'wb') as f:  
            f.write(content)

def unzip_right(filename):
    import zipfile, os
    exampleZip = zipfile.ZipFile(filename)
    exampleZip.extractall()
    exampleZip.close()
    pass

if __name__ == "__main__":
	  file_name = "./sysFin.zip"
	  url       = 'http://221.6.167.248:8082/sysFin' 
    http_down_right(url, file_name)
    unzip(file_name)
