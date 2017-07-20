#!/usr/bin/env python
#-*- coding:utf-8 -*-
from RedisHelper import RedisHelper

def sub():
	obj = RedisHelper()
	redis_sub = obj.subscribe()
	while True:
		msg = redis_sub.parse_response()
		print msg

def main():
	sub()

if __name__ == '__main__':
	main()
