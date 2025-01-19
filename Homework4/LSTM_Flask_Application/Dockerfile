FROM python:3.9-slim

WORKDIR /app


RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

RUN mkdir -p models/models scalers/scalers

COPY . .

EXPOSE 6002

CMD ["python", "app.py"]