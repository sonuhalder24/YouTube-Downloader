import sys
from pytube import YouTube
myVideo=YouTube(sys.argv[1])
print(myVideo.streams.get_audio_only().url)
