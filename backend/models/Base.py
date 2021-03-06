from mongoengine import *
from typing import Optional, TypeVar, Union, get_type_hints
import datetime
from mongoengine import connection
from mongoengine.connection import connect, disconnect
from mongoengine.fields import *
from mongoengine.pymongo_support import *
from mongoengine.context_managers import *
from mongoengine.document import *

INVISIBLE = TypeVar('INVISIBLE')
depth_limit = 10 # 防止自我引用无限递归炸鸡

class Base():
    """需要和mongoengine的Document多继承配套使用"""
    @staticmethod
    def expand_mono(obj, depth):
        if depth>depth_limit:
            return None
        if hasattr(obj, 'get_base_info'):
            return getattr(obj, 'get_base_info')(depth=depth+1)
        else:
            return obj
    def get_base_info(self, depth=0, *args):
        try:
            d = {}
            for k in self._fields_ordered:
                if get_type_hints(self).get(k, None) == INVISIBLE:
                    continue
                selfk = getattr(self, k)
                if isinstance(selfk, list):
                    for i in selfk:
                        d.setdefault(k, []).append(Base.expand_mono(i, depth))
                else:
                    d[k] = Base.expand_mono(selfk, depth)
            d['id'] = str(self.id)
            return d
        except: # 不加注解上面会报错
            return self.get_all_info(depth=depth)
    def get_all_info(self, depth=0, *args):
        d = {} 
        for k in self._fields_ordered:
            selfk = getattr(self, k)
            if isinstance(selfk, list):
                for i in selfk:
                    d.setdefault(k, []).append(Base.expand_mono(i, depth))
            else:
                d[k] = Base.expand_mono(selfk, depth)
        if hasattr(self, 'id'):
            d['id'] = str(self.id)
        return d
    @classmethod
    def chk(cls, pk):
        """确保对象存在，如不存在则创建一个，返回给定主键确定的对象"""
        if isinstance(pk, cls):
            return pk
        tmp = cls.objects(pk=pk).first()
        if not tmp:
            return cls(pk=pk).save()
        return tmp
    @classmethod
    def trychk(cls, pk):
        """若对象存在，返回主键对应的对象，否则返回None"""
        if isinstance(pk, cls):
            return pk
        tmp = cls.objects(pk=pk).first()
        if not tmp:
            return None
        return tmp

class SaveTimeBase(Base):
    create_time = DateTimeField()
    def save_changes(self):
        return self.save()
    def first_create(self):
        self.create_time = datetime.datetime.now()
        return self.save_changes()
    
    def get_base_info(self, *args):
        d = super().get_base_info(*args)
        d['create_time'] = self.create_time.strftime('%Y-%m-%d')
        return d

    def get_all_info(self, *args):
        d = super().get_all_info(*args)
        d['create_time'] = self.create_time.strftime('%Y-%m-%d')
        return d
