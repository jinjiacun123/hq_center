#coding:utf-8
import os
import struct
import sys
from optparse import OptionParser
from help import timestamp_datetime
from help import data_to_mysql

#get file's name from folder
def each_file(file_path, type='send'):
	global time_diff_time
	global time_list
	path_dir = os.listdir(file_path)
	for all_dir in path_dir:
		child = os.path.join('%s%s' %(file_path, all_dir))
#		print 'child:', all_dir[0:10]
		get_time  = int(all_dir[0:10])
		if type == 'send':	
			send_time = get_send_time(file_path, all_dir)
			diff_time = get_time - send_time
			if not time_list.has_key(diff_time):
				time_list[diff_time] = [1,all_dir,timestamp_datetime(get_time)]
			else:
				time_list[diff_time][0] = time_list[diff_time][0] + 1
			print "file_name:%s,get_time:%d,send_time:%d" % (child, get_time, send_time)
			time_diff_list.append({'file':all_dir,'time':get_time - send_time})
		elif type == 'pull':
			pull_time = get_pull_time(file_path, all_dir)
			diff_time = get_time - pull_time
			print "file_name:%s,get_time:%d,pull_time:%d" % (child, get_time, pull_time)
			#t['file'] = all_dir
			#t['time'] = get_time - send_time
			if not time_list.has_key(diff_time):
				time_list[diff_time] = [1,all_dir,timestamp_datetime(get_time)]
			else:
				time_list[diff_time][0] = time_list[diff_time][0] + 1
			time_diff_list.append({'file':all_dir,'time':get_time - pull_time})
		elif type == 'mysql':
			package_info = get_package_info(file_path, all_dir, get_time)



#get file size
def get_file_size(file_path, file_name):
	file_full_path = file_path + file_name
	try:
		size = os.path.getsize(file_full_path)
	except Exception as err:
		return 0
	return size

#get send time
def get_send_time(file_path, file_name):
	send_time = 0
#	print 'file_full_path:',file_path+file_name
	fh = open(file_path+file_name, "rb")
	my_buf = fh.read(8)
#	print "%d\n" % (my_buf)
	t = struct.unpack("2i", my_buf)
	send_time = t[1]
	fh.close()
	return send_time

#get pull time
def get_pull_time(file_path, file_name):
	pull_time = 0
	fh = open(file_path+file_name, "rb")
	my_buf = fh.read(12)
	t = struct.unpack("3i", my_buf)
	pull_time = t[2]
	fh.close()
	return pull_time

#get package info
#
#@return pull_time:分笔时间戳
#        send_time:转发时间
#        package_count:包条数
def get_package_info(file_path, file_name, cur_time):
	send_time = 0
	pull_time = 0
	package_count = 0
	package_off = 0
	is_continue = False
	
	fh = open(file_path + file_name, "rb")
	try:
		size = os.path.getsize(file_path+file_name)
	except Exception as err:
		print err
		return False
	my_buf = fh.read(12)
	t = struct.unpack("3i", my_buf)
	package_count = t[0]
	send_time     = t[1]
	pull_time     = t[2]
	package_list.append((cur_time, send_time, pull_time, package_count))

	#check is an package or more	
	if package_count * 144 + 8 <> size:
		off = 144 * package_count -4
		size = size - off - 12
		while size > 0:
		#is not one package
			if is_continue:
				fh.read(off)
				send_time     = 0
				pull_time     = 0
				package_count = 0
				my_buf = fh.read(12)
				#get some time and count
				t = struct.unpack("3i", my_buf)
				send_time = t[1]
				pull_time = t[2]
				package_list.append((cur_time, send_time, pull_time, package_count))
				off = 144 * package_count -4
				size = size - off -12
			else:
				break
	

	fh.close()

def analyse_send(file_path):
	max_time = 0
	max_time_file = ''
	min_time = 10
	avg_time = 0.00
	sum_time = 0.00

	each_file(file_path)
	print time_diff_list
	for i in range(0, len(time_diff_list)):
		cur_time = time_diff_list[i]['time']
		sum_time = sum_time + cur_time
		if max_time < cur_time:
			max_time = cur_time
			max_time_file = time_diff_list[i]['file']
		if min_time > cur_time:
			min_time = cur_time
	avg_time = sum_time / len(time_diff_list)
	print [(k, time_list[k]) for k in sorted(time_list.keys())]
	print "max_time:%d,file:%s,max_count:%d,min_time:%d,avg_time:%f\n" % (max_time, 
																		  max_time_file,
																		  time_list[max_time][0],
																		  min_time, 
																		  avg_time)

def analyse_pull(file_path):
	max_time = 0
	max_time_file = ''
	min_time = 10
	avg_time = 0.00
	sum_time = 0.00

	each_file(file_path, "pull")
	print time_diff_list
	for i in range(0, len(time_diff_list)):
		cur_time = time_diff_list[i]['time']
		sum_time = sum_time + cur_time
		if max_time < cur_time:
			max_time = cur_time
			max_time_file = time_diff_list[i]['file']
		if min_time > cur_time:
			min_time = cur_time

	avg_time = sum_time / len(time_diff_list)
	#print 'time_list:',time_list.items()
	keys = time_list.keys();
	keys.sort()
	print 'time_list:',[(k, time_list[k]) for k in sorted(time_list.keys())]
	'''
	keys = time_list.keys()
	keys.sort()
	print 'sort time_list:', [time_list[key] for key in keys]
	'''
	print "max_time:%d,file:%s,max_count:%d,min_time:%d,avg_time:%f\n" %(max_time, max_time_file, time_list[max_time][0], min_time, avg_time)

def to_mysql(file_path):
	each_file(file_path, 'mysql')
	print 'package_list:', package_list
	data_to_mysql('insert into package(my_time, lou_time, source_time, tick_count) values(%d,%d,%d,%d)', package_list, {'host':'192.168.1.233', 
		                           'port':3306, 
								   'user':'root',
								   'passwd':'123456',
								   'db':'test'})
	print 'to mysql finish'

def main():
	global time_diff_list
	global time_list
	global package_list
	time_diff_list = []
	time_list      = {}
	package_list   = []

	parser = OptionParser()
	parser.add_option("-s", "--send",    action="store",
									     dest="send",
									     type="string",
									     help="send time analyse")
	parser.add_option("-p", "--pull",    action="store",
									     dest="pull",
									     type="string",
									     help="pull time analyse")
	parser.add_option("-d", "--dbMysql", action="store",
										 dest="mysql",
										 type="string",
										 help="save to mysql")
	(options, args) = parser.parse_args()
	if options.send is not None:
		analyse_send(options.send)
	if options.pull is not None:
		analyse_pull(options.pull)
	if options.mysql is not None:
		to_mysql(options.mysql)

if __name__ == '__main__':
	main()
