#!/usr/bin/env python
#-*- coding:utf-8 -*-
import sys
import struct
from bitstring import BitArray, BitStream
import array
import os
import Queue
import threading
from optparse import OptionParser
import time
import MySQLdb

#safe queue
class concurrent_queue:
	def __init__(self, capacity = -1):
		self.__capacity = capacity
		self.__mutex    = threading.Lock()
		self.__cond     = threading.Condition(self.__mutex)
		self.__queue    = Queue.Queue()

	def get(self):
		if self.__cond.acquire():
			while self.__queue.empty():
				self.__cond.wait()

			elem = self.__queue.get()
			self.__cond.notify()
			self.__cond.release()
		return elem

	def put(self, elem):
		if self.__cond.acquire():
			while self.__queue.qsize() >= self.__capacity:
				self.__cond.wait()
			self.__queue.put(elem)

			self.__cond.notify()
			self.__cond.release()

	def clear(self):
		if self.__cond.acquire():
			self.__queue.queue.clear()
			self.__cond.release()
			self.__cond.notifyAll()

	def empty(self):
		is_empty = False
		if self.__mutex.acquire():
			is_empty = self.__queue.empty()
			self.__mutex.release()

		return is_empty

	def size(self):
		size = 0
		if self.__mutex.acquire():
			size = self.__queue.qsize()
			self.__mutex.release()

		return size

	def resize(self, capacity = -1):
		self.__capacity = capacity

#test multhread safe queue
class cq_test2():
	def __init__(self):
		self.queue = concurrent_queue(10)

	def consumer(self):
		while True:
			task = self.queue.get()
			print 'get',task,' from queue'

	def producter(self):
		while True:
			for i in range(10):
				self.queue.put(i)

	def run(self):
		t1 = threading.Thread(target = self.consumer)
		t2 = threading.Thread(target = self.producter)

		t1.start()
		t2.start()


def test_main():
	cq_test = cq_test2()
	cq_test.run()


def to_byte(file_path):
#	fh = open("./bit_demo.txt", "rb")
#	fh = open("./data", "r")
	#fh = open('./2017-07-17-09-55-09.data', "rb")
	package = 0
	fh = open(file_path, "rb")
	while True:
		my_buf = fh.read(8)
		if my_buf:
			(record_len,send_time) = struct.unpack("2i", my_buf)
#		if record_len:
#		file_size = os.path.getsize(sys.argv[1])
#	record_len = file_size/144
	#my_buf = fh.read(4)
	#(record_len,) = struct.unpack("i", my_buf)
	#record_len = struct.unpack("i", BitArray('0b'+my_str).bytes)
			print 'send_time:%d,record_len:%d' % (send_time,record_len)
			for i in range(0, record_len):
				my_buf = fh.read(144)
				t = struct.unpack('2i10s10s29f', my_buf)
				market = t[2]
				code   = t[3]
				print 'package:%d,i:%d,market:%s,code:%s,'% (package,i,str(market), code)
				print t
		else:
			break
		package = package +1
	fh.close()
	exit()

	my_buf = fh.read(4)
	i = 0;
	while True:
		my_buf = fh.read(144)
		t = struct.unpack('2i10s10s29f', my_buf)
		print 'i:',i,',t:',t
		i = i + 1
	exit()
	print 'record_len:',record_len
	for i in range(0, record_len[0]):
		record_len = 4*8*2+10*8*2+4*8*29
		my_str = fh.read(record_len)
		tar_str = ''
		tar_str = my_str
		print 'tar_str len:',len(tar_str)
		bit_array = BitArray('0b' + tar_str)
		t = struct.unpack('2i10s10c29f', bit_array.bytes)
		print  'i:',i,',t:',t

def reverse(s):
	return s[::-1]

def timestamp_datetime(value):
	format = '%Y-%m-%d %H:%M:%S'
	value = time.localtime(value)
	dt = time.strftime(format, value)
	return dt

def datetime_timestamp(dt):
	time.strptime(dt, '%Y-%m-%d %H:%M:%S')
	s = time.mktime(time.strptime(dt, '%Y-%m-%d %H:%M:%S'))
	return int(s)

#insert into mysql
def data_to_mysql(sql_template, data, db_info):
	conn = MySQLdb.connect(host=db_info['host'], 
						   port=db_info['port'], 
						   user=db_info['user'], 
						   passwd=db_info['passwd'],
						   db=db_info['db'])

	cur =conn.cursor()

	for item in data:	
		cur.execute(sql_template % item);	

	cur.close()
	conn.commit()
	conn.close()
	pass

def main():
	parser = OptionParser()
	parser.add_option("-p", "--pdbk", action="store",       dest='pdcl', type='string',                  help="parse binary data")
	parser.add_option("-t", "--testq", action="store_true", dest='testq',               default="True",  help="test multhread queue") 
	(options, args) = parser.parse_args()
	if options.pdcl is not None:
		#print 'pdcl is true,data:',options
		to_byte(options.pdcl)
	if options.testq is True:
		test_main()

if __name__ == '__main__':
	main()
