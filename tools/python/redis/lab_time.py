#coding:utf-8
import os
import struct
import sys

#get file's name from folder
def each_file(file_path):
	global time_diff_time
	path_dir = os.listdir(file_path)
	for all_dir in path_dir:
		child = os.path.join('%s%s' %(file_path, all_dir))
#		print 'child:', all_dir[0:10]
		get_time  = int(all_dir[0:10])
		send_time = get_send_time(file_path, all_dir)
		print "file_name:%s,get_time:%d,send_time:%d" % (child, get_time, send_time)
		#t['file'] = all_dir
		#t['time'] = get_time - send_time
		time_diff_list.append({'file':all_dir,'time':get_time - send_time})


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

def main():
	global time_diff_list
	time_diff_list = []
	max_time = 0
	max_time_file = ''
	min_time = 10
	avg_time = 0.00
	sum_time = 0.00
	file_path = sys.argv[1]


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
	print "max_time:%d,file:%s,min_time:%d,avg_time:%f\n" % (max_time, max_time_file, min_time, avg_time)

if __name__ == '__main__':
	main()
