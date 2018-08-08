module.exports = {
  devServer: {
    proxy: {
      '/usersearch': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/search': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/login': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/usercourses': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/userdocuments': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/validateToken': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/crawler': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/upload': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/display': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/courses': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/accountType': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/subscribeCourse': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/deleteDocument': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/userData': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/courseData': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  }
}