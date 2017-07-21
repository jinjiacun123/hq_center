#!/usr/bin/env python
#-*- coding:utf-8 -*-
import redis
import struct
import datetime
from optparse import OptionParser
import time
import os
from help import concurrent_queue
import threading

class hq_test():
	def __init__(self):
		self.queue = concurrent_queue(10000)
	
	def consumer(self):
		while True:
			data = self.queue.get()
			#print 'data:',data
			save("./"+data['time']+".my_test_data", data['data'])
			record_len = len(data['data'])/144
			#print 'record_len:',record_len
			for i in range(0, record_len):
				buff = data['data'][i*144:i*144+144]
				t = struct.unpack('2i10s10s29f', buff)
				#print t

	def product(self):
		rc = redis.Redis(host='112.84.186.241', port=6379)
		ps = rc.pubsub()
		ps.subscribe(['stest1', 'eg'])
		index = 0
		while True:
			for item in ps.listen():
				if item['type'] == 'message':
					my_buf = item['data']
					buf    = my_buf[0:4]
					t = struct.unpack("i",buf)
					print 'index:',index,',t:',t[0],',cur_time',str(time.time())
					self.queue.put({'time':datetime.datetime.now().strftime("%Y-%m-%d-%H-%M-%S"),'data':item['data']})
					index = index + 1

	def run(self):
		t1 = threading.Thread(target = self.consumer)
		t2 = threading.Thread(target = self.product)
		t1.start()
		t2.start()

class hq():
	def __init__(self):
		self.queue = concurrent_queue(10)
		self.my_dir = "./" + datetime.datetime.now().strftime("%Y-%m-%d") + "/"
		if os.path.exists(self.my_dir) == False:
			os.makedirs(self.my_dir)
	
	def consumer(self):
		while True:
			data = self.queue.get()
		#	print 'dir:',self.my_dir,',data:',data
			save(self.my_dir+data['time']+".data", data['data'])

	def product(self):
		rc = redis.Redis(host='112.84.186.241', port=6379)
		ps = rc.pubsub()
		ps.subscribe(['1100', '1200'])
		for item in ps.listen():
			if item['type'] == 'message':
				self.queue.put({'time':str(time.time()),'data':item['data']})
				print 'recive a message'

	def run(self):
		t1 = threading.Thread(target = self.consumer)
		t2 = threading.Thread(target = self.product)
		t1.start()
		t2.start()

def test_subscribe():
	rc = redis.Redis(host='112.84.186.241', port=6379)
	ps = rc.pubsub()
	ps.subscribe(['stest1','eg'])
	for item in ps.listen():
		if item['type'] == 'message':
			save("./"+datetime.datetime.now().strftime("%Y-%m-%d-%H-%M-%S")+".my_test_data", item['data'])
			record_len = len(item['data'])/144
			print 'record_len:', record_len
			for i in range(0, record_len):
				buff = item['data'][i*144:i*144+144]
				t = struct.unpack('2i10s10s29f',buff)
				print t

def get_hq():
	rc = redis.Redis(host='112.84.186.241', port=6379)
	ps = rc.pubsub()
	my_dir = "./"+datetime.datetime.now().strftime("%Y-%m-%d")+"/"
	if os.path.exists(my_dir) == False:
		os.makedirs(my_dir)
	ps.subscribe(['1100', '1200'])
	for item in ps.listen():
		if item['type'] == 'message':
			print item['data']
			#save("./"+datetime.datetime.now().strftime("%Y-%m-%d-%H-%M-%S")+".data", item['data'])
			save(my_dir+str(time.time())+".data", item['data'])

def save(filename, contents):
	fh = open(filename, "a")
	fh.write(contents)
	fh.close()

def main():
	parser = OptionParser()
	parser.add_option("-g",  "--get-redis",         action="store_true", dest="get_redis",  
			                 default="True", help="get hq info from redis publish")
	parser.add_option("-t",  "--test-redis",        action="store_true", dest="test_redis", 
			                 default="True", help="get test info from redis publish")
	parser.add_option("-G","--get-redis-thread",  action="store_true", dest="get_redis_thread",
			                 default="True", help="multhreading get hq info from redis publish")
	parser.add_option("-T","--test-redis-thread", action="store_true", dest="test_redis_thread",
							 default="True", help="multhreading get test info from redis publish")
	(options, args) = parser.parse_args()
	if options.get_redis is True:
		get_hq()
	if options.test_redis is True:
		test_subscribe()
	if options.get_redis_thread is True:
		my_hq = hq()
		my_hq.run()
	if options.test_redis_thread is True:
		my_test = hq_test()
		my_test.run()

if __name__ == '__main__':
	main()
