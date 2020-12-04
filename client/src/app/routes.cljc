(ns app.routes)

(def data
  {:-      :app.page.book.model/page
   "content" {:- :app.page.content.model/index}
   "book"    {:- :app.page.book.model/page
              'page {:- :app.page.book.model/page}}})
