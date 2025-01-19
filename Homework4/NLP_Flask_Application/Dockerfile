FROM python:3.8-slim
WORKDIR /app
RUN apt-get update && apt-get install -y curl
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
COPY sentiment_analysis_results1.csv .
EXPOSE 6001
CMD ["python", "app.py"]