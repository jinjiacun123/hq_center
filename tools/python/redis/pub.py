#!/usr/bin/env python
#-*- coding:utf-8 -*-
from RedisHelper import RedisHelper

def pub():
	obj = RedisHelper()
	obj.publish('hello')

def main():
	pub()

if __name__ == '__main__':
	main()
