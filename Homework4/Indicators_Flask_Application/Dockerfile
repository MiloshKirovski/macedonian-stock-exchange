FROM python:3.8-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY . .


RUN apt-get update && apt-get install -y curl

EXPOSE 5000

CMD ["python", "app.py"]