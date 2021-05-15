
const express=require('express');
const app=express();

app.use(function(req,res,next){
   var _send = res.send;
   var sent = false;
   res.send = function(data){
       if(sent) return;
       _send.bind(res)(data);
       sent = true;
   };
   next();
   });

app.listen(process.env.PORT||3000,()=>{
    console.log("Sever is running!!"+process.env.PORT||3000);
});

app.get('/video',(req,res)=>{
    console.log("Hit video");
    var spawn=require('child_process').spawn;
    var process=spawn('python', ["./video.py", req.query.link]);
    process.stdout.on('data',(data)=>{
        var vd=data.toString();
        console.log(vd);
        res.send(vd);
        
    });
    console.log("Video request is processing");
});

app.get('/audio',(req,res)=>{
    console.log("Hit audio");
    var spawn=require('child_process').spawn;
    var process=spawn('python',["./audio.py",req.query.link]);
    process.stdout.on('data',(data)=>{
        var ad=data.toString();
        console.log(ad);
        res.send(ad);       
    });
    console.log("Audio request is processing");
});

app.get('/server-initialize',(req,res)=>{
    console.log("Server wake up");
    res.send("true");
});

