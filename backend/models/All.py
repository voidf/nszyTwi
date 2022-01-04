from mongoengine import *
from mongoengine.queryset.base import *
from models.Base import *

class User(Base, Document):
    username = StringField(primary_key=True)
    password: INVISIBLE = StringField()
    avatar = StringField()
    desc = StringField()
    follows = ListField(ReferenceField('User', reverse_delete_rule=PULL), default=[])

    @classmethod
    def chk(cls, pk):
        return super().chk(pk)
    @classmethod
    def trychk(cls, pk):
        return super().trychk(pk)

    def get_base_info_excluded(self):
        return {
            'id': self.username,
            'username': self.username,
            'avatar': self.avatar,
            'desc': self.desc
        }

    def get_base_info(self, depth=0, *args):
        d = self.get_base_info_excluded()
        d['follows'] = [i.get_base_info_excluded() for i in self.follows]
        return d

    def get_all_info(self, depth=0, *args):
        d = self.get_base_info_excluded()
        d['follows'] = [i.get_base_info_excluded() for i in self.follows]
        return d

    # def __int__(self):
    #     return int(self.username)
    def __str__(self):
        return str(self.username)

class Twi(Base, Document):
    content = StringField()
    author = ReferenceField(User, reverse_delete_rule=CASCADE)
    comments = ListField(ReferenceField('Twi', reverse_delete_rule=PULL), default=[])
    is_top = BooleanField(default=True)
    post_time = IntField()
    # attachments = ListField()
    