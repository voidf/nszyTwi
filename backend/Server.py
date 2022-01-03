from fastapi import FastAPI, Depends
from webcfg import db, web_host, web_port

import os, sys
def create_fastapi() -> FastAPI:
    from mongoengine import connect
    app = FastAPI(version="1.0.0", title="Twi")
    connect(**db)

    # fapi.G.first_time = True
    # from fapi.models.Routinuer import Routiner
    # Routiner
    if os.getcwd() not in sys.path:
        sys.path.append(os.getcwd())
    print(sys.path)


    from routers import master_router
    app.include_router(master_router)
    
    return app

app = create_fastapi()

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(
        app,
        host=web_host,
        port=web_port,
    )