import hashlib
from webcfg import salt, jwt_key
from models.All import *
from jose import JWTError, jwt
import G
from G import g
import os
from typing import Union
from loguru import logger
import asyncio
import traceback

def encrypt(s: str) -> str:
    return hashlib.sha256((s + salt).encode('utf-8')).hexdigest()


def generate_jwt(adapter: Union[User, str], expire_seconds: float = 120.0):
    token_dict = {
        'id': str(adapter.pk) if isinstance(adapter, User) else adapter,
        'ts': str((datetime.datetime.now()+ datetime.timedelta(seconds=expire_seconds)).timestamp())
    }
    return jwt.encode(
        token_dict,  # payload, 有效载体
        jwt_key,  # 进行加密签名的密钥
    )

def verify_jwt(token):
    try:
        payload = jwt.decode(token, jwt_key)
        if datetime.datetime.now().timestamp() > float(payload['ts']):
            return None, "令牌过期"
        adapter = User.objects(pk=payload['id']).first()
        if not adapter:
            return None, "无此用户"
        return adapter, ""
    except:
        traceback.print_exc()
        return None, "数据错误"

def trueReturn(data=None, msg=""):
    return {
        'data': data,
        'msg': msg,
        'status': True
    }

from fastapi import HTTPException

def falseReturn(code=500, msg="", data=None):
    raise HTTPException(code, {
        'data': data,
        'msg': msg,
        'status': False
    })
from fastapi import Request, HTTPException, Cookie
async def general_before_request(auth: Request):
    """请求预处理，将令牌放入线程作用域g()"""
    try:
        logger.debug('{} {}', auth.client.host, str(datetime.datetime.now()))
        # print(auth.client.host, str(datetime.datetime.now()))
        Authorization = auth.headers.get('Authorization', None)
        # print(Authorization, auth.cookies)
        if Authorization is None:
            Authorization = auth.cookies.get('Authorization', None)
        if Authorization:
            g().user, g().msg = verify_jwt(Authorization)
            logger.debug('{} requesting...', g().user.username)
        else:
            pass
    except:
        traceback.print_exc()
        raise HTTPException(400, "数据错误")


async def validsign():
    """验证用户是否登录"""
    if not hasattr(g(), 'user') or not g().user:
        raise HTTPException(401, '此操作需要登陆;'+getattr(g(), 'msg', ""))