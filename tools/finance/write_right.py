import MySQLdb
import struct 
 
 
def write_to_db(code,publish_data,giving_stock,placing_stock,giving_price,bonus):
	try:
	    conn=MySQLdb.connect(host='192.168.1.233',user='root',passwd='root',db='quote_gp',port=3306)
	    cur=conn.cursor()
	    #cur.execute('select * from `right`')
	    sqli="insert into `right`(symbol,publish_date,giving_stock,placing_stock,giving_price,bonus) values(%s,%s,%s,%s,%s,%s)"
	    cur.execute(sqli,(code,publish_data,giving_stock,placing_stock,giving_price,bonus))
	    #cur.execute("insert into `ritht`(symbol,publish_date,giving_stock,placing_Stock,giving_price,bonus) values()")
	    cur.close()
	    conn.close()
	except MySQLdb.Error,e:
	     print "Mysql Error %d: %s" % (e.args[0], e.args[1])
	     
	     
def read_pwr():
        file = open(r'F:\tools\LiuWeiStockWebServer\test\right.pwr', "rb")
        file.seek(12,0)
        while True:
                code = file.read(8)
                print code,':'
                file.seek(8,1)
                bytes = file.read(4)
                time, = struct.unpack("i", bytes)
                while time > 0:
                        bytes = file.read(16)
                        (giving_stock,placing_stock,giving_price,bonus) = struct.unpack("ffff",bytes) 
                        print "%d,%f,%f,%f,%f" % (time,giving_stock,placing_stock,giving_price,bonus)
                        write_to_db(code, 
                                    time, 
                                    round(giving_stock,2),
                                    round(placing_stock),
                                    round(giving_price),
                                    float('%.2f' % bonus))
                        bytes = file.read(4)
                        time, = struct.unpack("i", bytes)
	pass
	
	
def main():
       # write_to_db('HK00884',1493078400,0.000000,0.000000,0.000000,0.130000)
        read_pwr()
       
	pass
	



if __name__ == "__main__":
	main()
