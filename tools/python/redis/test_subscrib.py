#!/usr/bin/env python
#-*- coding:utf-8 -*-
import redis
import struct
import datetime
#r = redis.Redis(host='112.84.186.241', port=6379)
#print r


'''
from rediscluster import StrictRedisCluster

def main():
	startup_nodes = [{"host": "112.84.186.241", "port": "6379"}]
	rc = StrictRedisCluster(startup_nodes=startup_nodes, decode_responses=True)
	ps=rc.pubsub()
	ps.subscribe(['1100', '1200'])
	for item in ps.listen():
		if item['type'] == 'message':
			print item['data']

if __name__ == '__main__':
	main()
'''

import redis
import time
import os

def test_subscribe():
	rc = redis.Redis(host='112.84.186.241', port=6379)
	ps = rc.pubsub()
	ps.subscribe(['stest1','stest2','eg'])
	for item in ps.listen():
		if item['type'] == 'message':
			print 'channel:',item['channel']
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


def parse_struct(data):
	import struct
	package = 0
	my_buf = data[0:144]
	t = struct.unpack("2i10s10s29f", my_buf)
	print "t:",t
	pass

def save(filename, contents):
	fh = open(filename, "a")
	fh.write(contents)
	fh.close()

def main():
	test_subscribe()
#	get_hq()

if __name__ == '__main__':
	main()
