const BookApi = {
  getAllBooks: async () => {
    const res = await fetch("/books/all", { method: "GET" })
    return res.json();
  },
  getBookByName: async (name, token) => {
    const res = await fetch(`/books/${name}`, {
      method: "GET",
      headers: { "Authorization": "Bearer " + token },
    })
    const result = await res.json();
    console.log("got book " + result);
    return result;
  },
  addBook: async (data, token) => {
    const res = await fetch("/books/add", {
      method: "POST",
      body: JSON.stringify(data),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
      },
    })
    return res.json()
  },
  patchBookByIsbn: async (id, data, token) => {
    const res = await fetch(`/books/update/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token
      },
    })
    return res.json()
  },
  deleteBook: async (id, token) => {
    const res = await fetch(`/books/delete/${id}`, {
      method: "DELETE",
      headers: { "Authorization": "Bearer " + token }
    })
    return res
  },
}

module.exports = { BookApi }
