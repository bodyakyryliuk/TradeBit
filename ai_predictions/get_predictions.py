import numpy as np
import time as tm
import datetime as dt
from datetime import datetime as datt
import pandas as pd
import tensorflow as tf
import json
# Data preparation
from yahoo_fin import stock_info as yf
from sklearn.preprocessing import MinMaxScaler
from collections import deque

# AI
from keras.models import Sequential
from keras.layers import Dense, LSTM, Dropout

# Graphics library
import matplotlib.pyplot as plt


# SETTINGS
class get_predictions():
	def __init__(self, STOCK, daysTrain, epochs):
		# Window size or the sequence length, 7 (1 week)
		N_STEPS = 7

		# Lookup steps, 1 is the next day, 3 = after tomorrow
		LOOKUP_STEPS = [1,2,3]
		self.STOCK=STOCK
		self.days=daysTrain
		self.epochs=epochs
		# Stock ticker, GOOGL
		#STOCK = 'GOOGL'

		# Current date
		date_now = tm.strftime('%Y-%m-%d')
		start_date = (dt.date.today() - dt.timedelta(days=self.days)).strftime('%Y-%m-%d')

		# LOAD DATA 
		# from yahoo_fin 
		# for 1104 bars with interval = 1d (one day)
		init_df = yf.get_data(
		    STOCK, 
		    start_date=start_date, 
		    end_date=date_now, 
		    interval='1d')

		#print (init_df)

		# remove columns which our neural network will not use
		init_df = init_df.drop(['open', 'high', 'low', 'adjclose', 'ticker', 'volume'], axis=1)
		# create the column 'date' based on index column
		init_df['date'] = init_df.index


		# Let's preliminary see our data on the graphic
		plt.style.use(style='ggplot')
		plt.figure(figsize=(16,10))
		plt.plot(init_df['close'][-200:])
		plt.xlabel('days')
		plt.ylabel('price')
		plt.legend([f'Actual price for {STOCK}'])
		plt.close()
		#plt.show() #actual price for the stock
		#input()
		# Scale data for ML engine
		scaler = MinMaxScaler()
		init_df['scaled_close'] = scaler.fit_transform(np.expand_dims(init_df['close'].values, axis=1))


		def PrepareData(days):
		  df = init_df.copy()
		  df['future'] = df['scaled_close'].shift(-days)
		  last_sequence = np.array(df[['scaled_close']].tail(days))
		  df.dropna(inplace=True)
		  sequence_data = []
		  sequences = deque(maxlen=N_STEPS)

		  for entry, target in zip(df[['scaled_close'] + ['date']].values, df['future'].values):
		      sequences.append(entry)
		      if len(sequences) == N_STEPS:
		          sequence_data.append([np.array(sequences), target])

		  last_sequence = list([s[:len(['scaled_close'])] for s in sequences]) + list(last_sequence)
		  last_sequence = np.array(last_sequence).astype(np.float32)

		  # construct the X's and Y's
		  X, Y = [], []
		  for seq, target in sequence_data:
		      X.append(seq)
		      Y.append(target)

		  # convert to numpy arrays
		  X = np.array(X)
		  Y = np.array(Y)

		  return df, last_sequence, X, Y


		#print(PrepareData(1))

		def GetTrainedModel(x_train, y_train):
		  model = Sequential()
		  model.add(LSTM(60, return_sequences=True, input_shape=(N_STEPS, len(['scaled_close']))))
		  model.add(Dropout(0.25))
		  model.add(LSTM(120, return_sequences=False))
		  model.add(Dropout(0.25))
		  model.add(Dense(35))
		  model.add(Dense(1))

		  BATCH_SIZE = 16
		  EPOCHS = self.epochs

		  model.compile(loss='mean_squared_error', optimizer='Adam')

		  model.fit(x_train, y_train,
		            batch_size=BATCH_SIZE,
		            epochs=EPOCHS,
		            verbose=1)

		  model.summary()
		  self.model=model
		  return self.model


		  # GET PREDICTIONS
		predictions = []

		for step in LOOKUP_STEPS:
		  df, last_sequence, x_train, y_train = PrepareData(step)
		  x_train = x_train[:, :, :len(['scaled_close'])].astype(np.float32)
		  #if (step==1):
		  	#model = GetTrainedModel(x_train, y_train)

		  self.model = GetTrainedModel(x_train, y_train)
		  last_sequence = last_sequence[-N_STEPS:]
		  last_sequence = np.expand_dims(last_sequence, axis=0)
		  prediction = self.model.predict(last_sequence)
		  predicted_price = scaler.inverse_transform(prediction)[0][0]

		  predictions.append(round(float(predicted_price), 2))



		if bool(predictions) == True and len(predictions) > 0:
			predictions_list = [str(d)+'$' for d in predictions]
			predictions_str = ', '.join(predictions_list)
			message = f'{STOCK} prediction for upcoming 3 days ({predictions_str})'
			print(message)
			#tm.sleep(2)


		 # Execute model for the whole history range
		copy_df = init_df.copy()
		y_predicted = self.model.predict(x_train)
		y_predicted_transformed = np.squeeze(scaler.inverse_transform(y_predicted))
		first_seq = scaler.inverse_transform(np.expand_dims(y_train[:6], axis=1))
		last_seq = scaler.inverse_transform(np.expand_dims(y_train[-3:], axis=1))
		y_predicted_transformed = np.append(first_seq, y_predicted_transformed)
		y_predicted_transformed = np.append(y_predicted_transformed, last_seq)
		copy_df[f'predicted_close'] = y_predicted_transformed

		#print(copy_df) table of the predictions vs correct closes
		#print((copy_df['date'].iloc[-1]))
		#input()
		# Add predicted results to the table
		date_now = copy_df['date'].iloc[-1]
		'''
		print(date_now)
		print(date_now+ pd.Timedelta(days=1))
		print(date_now+dt.timedelta(days=1))
		input()
		'''

		date_now = dt.date.today()
		date_tomorrow = dt.date.today() + dt.timedelta(days=1)
		date_after_tomorrow = dt.date.today() + dt.timedelta(days=2)

		copy_df.loc[date_now] = [predictions[0], f'{date_now}', 0, 0]
		copy_df.loc[date_tomorrow] = [predictions[1], f'{date_tomorrow}', 0, 0]
		copy_df.loc[date_after_tomorrow] = [predictions[2], f'{date_after_tomorrow}', 0, 0]


		# Ensure the index is a DateTimeIndex
		copy_df.index = pd.to_datetime(copy_df.index)
		# Extract the last four values
		last_four_values = copy_df['close'][-4:]
		# Extract the fourth-to-last value (unshifted)
		unshifted_value = last_four_values.iloc[0]
		# Shift the indices of the last three values by one day
		shifted_values = last_four_values.iloc[1:].shift(1, freq='D')
		# Combine the unshifted value with the shifted ones
		new_data = pd.concat([pd.Series([unshifted_value], index=[last_four_values.index[0]]), shifted_values])
		# Create a new DataFrame from this combined series
		new_df = pd.DataFrame(new_data, columns=['close'])


		fig_days=30

		# Result chart
		plt.style.use(style='ggplot')
		plt.figure(figsize=(16,10))
		plt.plot(copy_df['close'][-fig_days:].head(fig_days-3))
		plt.plot(copy_df['predicted_close'][-fig_days:].head(fig_days-3), linewidth=1, linestyle='dashed')
		copy_df.index=pd.to_datetime(copy_df.index)

		#copy_df['close'][-3:]=copy_df['close'][-3:].shift(1,freq='D')
		#lets try to work on this
		#plt.plot(copy_df['close'][-30].tail(4))
		plt.plot(new_df['close'])
		plt.xlabel('days')
		plt.ylabel('price')
		plt.legend([f'Actual price for {STOCK}', 
		            f'Predicted price for {STOCK}',
		            f'Predicted price for future 3 days'])
		print("\n\n...plotted...\n\n")
		plt.savefig(f"saved_predictions_{self.epochs}_epochs/{self.STOCK}_{self.days}_days_{self.epochs}_epochs")
		#input()
		#plt.show()


		#write the values to file

		

		dates = copy_df.index[-fig_days:].tolist()[:fig_days-3]
		formatted_dates = [date.strftime('%Y-%m-%d') for date in dates]
		predicted_prices =copy_df['predicted_close'][-fig_days:].head(fig_days-3)
		timestamps = formatted_dates
		self.past_pred_data = [{"predictedPrice": price, "timestamp": timestamp} for price, timestamp in zip(predicted_prices, timestamps)]

		dates = new_df.index.tolist()
		formatted_dates = [date.strftime('%Y-%m-%d') for date in dates]
		predicted_prices =new_df['close']
		timestamps = formatted_dates
		self.future_pred_data = [{"predictedPrice": price, "timestamp": timestamp} for price, timestamp in zip(predicted_prices, timestamps)]


		self.save_params()

	def get_data(self):
		return self.past_pred_data, self.future_pred_data
		# Convert to JSON string
		#json_data = json.dumps(data, indent=4)


	def save_params(self):
		self.model.save(f'saved_models_{self.epochs}_epochs/{self.STOCK}_model_paramteres_{self.epochs}_epochs_{self.days}_days.h5')



'''
bunker: 


		# Printing the JSON string
		print(json_data)
		input()
		# If you want to save this to a file
		with open("data.json", "w") as file:
		    file.write(json_data)

		with open("tryy.txt", "w") as file:
		    # Writing the first slice of data
			dates = copy_df.index[-30:].tolist()[:27]
			formatted_dates = [date.strftime('%Y-%m-%d') for date in dates]
			
			for date, value in zip(dates, copy_df['close'][-30:].head(27)):
				file.write("{\n")
				file.write(f'"predictedPrice": {value},\n"timestamp": "{date}"')
				file.write("\n},\n")
			''
		    # Writing the second slice of data
			file.write(copy_df['predicted_close'][-30:].head(27).to_string())
			file.write("\n*\n")

		    # Writing the third slice of data
			file.write(copy_df['close'][-30:].tail(4).to_string())
			file.write("\n*\n")
			''

		#here


		
		with open(f"{STOCK}_Preds.json", "w") as file:
			with open("tryy.txt","r") as readFrom:
				for lines in readFrom:
					while lines!="*":
						file.write 



		# The input() at the end is not necessary for writing the file, 
		# it will just pause the script until you press Enter
		input()



		data = {
		    "actual_price": copy_df['close'][-150:].head(147).tolist(),
		    "predicted_price": copy_df['predicted_close'][-150:].head(147).tolist(),
		    "future_price": copy_df['close'][-150:].tail(4).tolist()
		}
		'
		copy_df['date'][-1]=daftt
		copy_df['date'][-2]=dtom
		copy_df=copy_df.drop('scaled_close',axis=1)


		copy_df['date'] = pd.to_datetime(df['date'], unit='ms').dt.strftime('%Y-%m-%d')



'''