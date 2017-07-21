#coding:utf-8
import os
import struct
import sys
from optparse import OptionParser
from help import timestamp_datetime

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
		else:
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

def main():
	global time_diff_list
	global time_list
	time_diff_list = []
	time_list      = {}

	parser = OptionParser()
	parser.add_option("-s", "--send", action="store",
									  dest="send",
									  type="string",
									  help="send time analyse")
	parser.add_option("-p", "--pull", action="store",
									  dest="pull",
									  type="string",
									  help="pull time analyse")
	(options, args) = parser.parse_args()
	if options.send is not None:
		analyse_send(options.send)
	if options.pull is not None:
		analyse_pull(options.pull)

if __name__ == '__main__':
	main()
