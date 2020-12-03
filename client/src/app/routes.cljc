(ns app.routes)

(def data
  {:-      :app.page.content.model/index
   "content" {:- :app.page.content.model/index}
   "book"    {:- :app.page.book.model/index
              'page {:- :app.page.book.model/page}}
   "test"    {:- :app.pages.auth.model/registration}})
