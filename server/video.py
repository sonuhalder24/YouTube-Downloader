import sys
from pytube import YouTube
myVideo=YouTube(sys.argv[1])
print(myVideo.streams.first().url)
