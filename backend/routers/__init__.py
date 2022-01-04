from pydantic import BaseModel
from fastapi import *
from models.All import *

from utils import *

master_router = APIRouter(
    tags=["master"],
    dependencies=[Depends(general_before_request)]
)

class login_form(BaseModel):
    username: str
    password: str
@master_router.post('/user/login')
async def legacy_auth(f: login_form, rsp: Response):
    a = User.objects(username=f.username).first()
    if not a:
        return falseReturn(401, '用户名或密码错误')

    if not encrypt(f.password) == a.password:
        return falseReturn(401, '用户名或密码错误')
    
    tk = generate_jwt(a, 86400)
    rsp.set_cookie("Authorization", tk, 86400)
    return {"access_token": tk, "token_type": "bearer"}

@master_router.post('/user/register')
async def register(f: login_form):
    if not User.objects(pk=f.username):
        User(
            username=f.username,
            password=encrypt(f.password)
        ).save()
        return trueReturn()
    return falseReturn(400, '用户名被占用')

@master_router.post('/user/profile', dependencies=[
    Depends(validsign)
])
async def profile(uid: str):
    u = User.objects(pk=uid).first()
    if not u:
        return falseReturn(404, '找不到该用户')
    return trueReturn(data={"info":u.get_base_info()})

@master_router.get('/user/me', dependencies=[
    Depends(validsign)
])
async def getme():
    return trueReturn(data={"info":g().user.get_base_info()})

from G import g

@master_router.get('/user/fo', dependencies=[
    Depends(validsign)
])
async def follow(uid: str):
    fu = User.objects(pk=uid).first()
    if not fu:
        return falseReturn(404, '找不到关注对象')
    g().user.update(add_to_set__follows=fu)
    return trueReturn()

@master_router.get('/user/unfo', dependencies=[
    Depends(validsign)
])
async def unfollow(uid: str):
    fu = User.objects(pk=uid).first()
    if not fu:
        return falseReturn(404, '找不到取消关注对象')
    g().user.update(pull__follows=fu)
    return trueReturn()


class new_twi_form(BaseModel):
    content: str

@master_router.post('/twi/new', dependencies=[
    Depends(validsign)
])
async def new_twi(f: new_twi_form):
    t = Twi(author=g().user, content=f.content, post_time=int(datetime.datetime.now().timestamp())).save()
    return trueReturn()


class comment_twi_form(BaseModel):
    tid: str
    content: str

@master_router.post('/twi/comment', dependencies=[
    Depends(validsign)
])
async def comment_twi(f: comment_twi_form):
    tg: Twi = Twi.objects(pk=f.tid).first()
    if not tg:
        return falseReturn(404, '找不到待评论推文')
    t = Twi(author=g().user, content=f.content, is_top=False, post_time=int(datetime.datetime.now().timestamp())).save()
    tg.comments.append(t)
    tg.save()
    return trueReturn()

@master_router.get('/twi/all', dependencies=[
    Depends(validsign)
])
async def all_twi():
    return trueReturn(
        data={
            'twis':[i.get_base_info() for i in Twi.objects(is_top=True).order_by('post_time')]
        }
    )

@master_router.get('/twi/follows', dependencies=[
    Depends(validsign)
])
async def follows_twi():
    fos = g().user.follows
    return trueReturn(
        data={
            'twis':[i.get_base_info() for i in Twi.objects(author__in=fos, is_top=True).order_by('post_time')]
        }
    )

@master_router.get('/twi/user', dependencies=[
    Depends(validsign)
])
async def user_twi(uid: str):
    a = User.objects(pk=uid).first()
    if not a:
        return falseReturn(404, '找不到给定用户')
    return trueReturn(
        data={
            'twis':[i.get_base_info() for i in Twi.objects(author=a, is_top=True).order_by('post_time')]
        }
    )

@master_router.get('/twi/detail', dependencies=[
    Depends(validsign)
])
async def single_twi(tid: str):
    a = Twi.objects(pk=tid).first()
    if not a:
        return falseReturn(404, '找不到给定推文')
    return trueReturn(
        data={
            'info': a.get_base_info()
        }
    )
