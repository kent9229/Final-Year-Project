# Final-Year-Project 

- Extract package sequences from existng projects
- Insert the library's name next to each package in the package sequence
- Train using deep4j word2vec
- Use word2vec model to find similar packages using in-build cosine similarity

- Get the libraries' vectors by averaging out the vectors of the packages within the library
- Find the difference for between each package and its library and perform a cosine simularity against the same with the target library

- Crawl all similar libraries' java documentation
- For both package and methods with desccriptions and their links
