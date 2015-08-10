import os
files = os.listdir(".");
lines = 0
chars = 0
while len(files) > 0:
	_file = files.pop(0);
	if(os.path.isdir(_file)):
		for _subfile in os.listdir(_file):
			files.append(_file + "/" + _subfile)
	else:
		_lines = open(_file, 'r').readlines()
		_chars = 0
		for line in _lines:
			_chars += len(line)
		print _file + ":" + str(len(_lines)) + ", " + str(_chars) + " @ " + str(_chars/len(_lines)) + "/line"
		lines += len(_lines)
		chars += _chars;
print "Total: " + str(lines) + " lines, " + str(chars) + " characters."
