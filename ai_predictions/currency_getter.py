import get_predictions
import json 


epochs=30
days1=1600
days2=1600


btc_pred=get_predictions.get_predictions(STOCK='BTC-USD', daysTrain=days1, epochs=epochs)
btc_pred, btc_future_pred=btc_pred.get_data()
ada_pred=get_predictions.get_predictions(STOCK='ADA-USD', daysTrain=days2, epochs=epochs)
ada_pred, ada_future_pred=ada_pred.get_data()
eth_pred=get_predictions.get_predictions(STOCK='ETH-USD', daysTrain=days1, epochs=epochs)
eth_pred, eth_future_pred=eth_pred.get_data()
bnb_pred=get_predictions.get_predictions(STOCK="BNB-USD",daysTrain=days1,epochs=epochs)
bnb_pred,bnb_future_pred=bnb_pred.get_data()
xrp_pred=get_predictions.get_predictions(STOCK="XRP-USD", daysTrain=days1,epochs=epochs)
xrp_pred,xrp_future_pred=xrp_pred.get_data()
sol_pred=get_predictions.get_predictions(STOCK="SOL-USD",daysTrain=days1,epochs=epochs)
sol_pred,sol_future_pred=sol_pred.get_data()
steth_pred=get_predictions.get_predictions(STOCK="STETH-USD",daysTrain=days1,epochs=epochs)
steth_pred,steth_future_pred=steth_pred.get_data()
googl_pred=get_predictions.get_predictions(STOCK='GOOGL', daysTrain=days2, epochs=epochs)
googl_pred, google_future_pred=googl_pred.get_data()
tsla_pred=get_predictions.get_predictions(STOCK="TSLA", daysTrain=days2, epochs=epochs)
tsla_pred,tsla_future_pred=tsla_pred.get_data()
aapl_pred=get_predictions.get_predictions(STOCK="AAPL", daysTrain=days2, epochs=epochs)
aapl_pred, aapl_future_pred=aapl_pred.get_data()
msft_pred=get_predictions.get_predictions(STOCK="MSFT", daysTrain=days1, epochs=epochs)
msft_pred, msft_future_pred=msft_pred.get_data()
amzn_pred=get_predictions.get_predictions(STOCK="AMZN", daysTrain=days1, epochs=epochs)
amzn_pred,amzn_future_pred=amzn_pred.get_data()
nvda_pred=get_predictions.get_predictions(STOCK="NVDA", daysTrain=days1, epochs=epochs)
nvda_pred, nvda_future_pred=nvda_pred.get_data()
meta_pred=get_predictions.get_predictions(STOCK="META", daysTrain=days1, epochs=epochs)
meta_pred, meta_future_pred=meta_pred.get_data()
jpm_pred=get_predictions.get_predictions(STOCK="JPM", daysTrain=days1, epochs=epochs)
jpm_pred, jpm_future_pred=jpm_pred.get_data()
f_pred=get_predictions.get_predictions(STOCK="F", daysTrain=days1,epochs=epochs)
f_pred,f_future_pred=f_pred.get_data()




crypto_pred = {
    "BTCUSDT": btc_pred,
    "ADAUSDT": ada_pred,
    "ETHUSDT": eth_pred,
    "BNBBUSDT": bnb_pred,
    "XRPUSDT": xrp_pred,
    "SOLUSDT": sol_pred,
    "STETHUSDT": steth_pred,
    "GOOGL": googl_pred,
    "TSLA": tsla_pred,
    "AAPL": aapl_pred,
    "MSFT": msft_pred,
    "AMZN": amzn_pred,
    "NVDA": nvda_pred,
    "META": meta_pred,
    "JPM": jpm_pred,
    "F": f_pred
}

crypto_future_pred={
    "BTCUSDT": btc_future_pred,
    "ADAUSDT": ada_future_pred,
    "ETHUSDT": eth_future_pred,
    "BNBBUSDT": bnb_future_pred,
    "XRPUSDT": xrp_future_pred,
    "SOLUSDT": sol_future_pred,
    "STETHUSDT": steth_future_pred,
    "GOOGL": google_future_pred,
    "TSLA": tsla_future_pred,
    "AAPL": aapl_future_pred,
    "MSFT": msft_future_pred,
    "AMZN": amzn_future_pred,
    "NVDA": nvda_future_pred,
    "META": meta_future_pred,
    "JPM": jpm_future_pred,
    "F": f_future_pred

}


# Convert to JSON string
json_data_pred = json.dumps(crypto_pred, indent=4)
json_data_future_pred=json.dumps(crypto_future_pred, indent=4)

# Printing the JSON string
#print(json_data)

# If you want to save this to a file
with open("crypto_predictions.json", "w") as file:
    file.write(json_data_pred)

with open("crypto_future_predictions.json", "w") as file:
    file.write(json_data_future_pred)

#important remark, only get the crypto prices during the day, at night the requests get stuck 