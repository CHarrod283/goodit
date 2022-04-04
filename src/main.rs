mod GooditDB;

use mysql::*;
use mysql::prelude::*;

use actix_web::{HttpServer, App, web, HttpResponse, Responder};
use actix_web::web::Data;
use tera::{Tera, Context};



async fn index(tera: web::Data<Tera>) -> impl Responder {
    let mut data = Context::new();
    let posts = [
        GooditDB::Post::new(

        ),

    ];
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        let tera = Tera::new("templates/**/*").unwrap();
        App::new()
            .app_data(Data::new(tera))
            .route("/", web::get().to(index))
    })
        .bind("127.0.0.1:8000")?
        .run()
        .await
}