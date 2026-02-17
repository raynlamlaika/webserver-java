import requests

# Define the URL and file path
url = "http://127.0.0.1:8181/upload"
file_paths = ["/home/rlamlaik/java/webserv/xss.md"]  # Replace with your file paths

for file_path in file_paths:
    try:
        with open(file_path, 'rb') as f:
            files = {'file': (file_path, f)}
            print(f"Uploading {file_path}...")
            response = requests.post(url, files=files)

            if response.status_code == 200:
                print(f"Successfully uploaded {file_path}: {response.text}")
            else:
                print(f"Failed to upload {file_path}: {response.status_code} - {response.text}")
    except FileNotFoundError:
        print(f"File not found: {file_path}")
    except Exception as e:
        print(f"An error occurred while uploading {file_path}: {e}")