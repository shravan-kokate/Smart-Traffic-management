from ultralytics import YOLO
import sys
import cv2
import argparse
import pyrebase
import supervision as sv

firebaseConfig = {
  "apiKey": "AIzaSyAUhrNGCfmzDFEQjgupILVmfFHZ8467v7s",
  "authDomain": "traffic-predictor-550ec.firebaseapp.com",
  "databaseURL": "https://traffic-predictor-550ec-default-rtdb.asia-southeast1.firebasedatabase.app",
  "projectId": "traffic-predictor-550ec",
  "storageBucket": "traffic-predictor-550ec.appspot.com",
  "messagingSenderId": "96893374979",
  "appId": "1:96893374979:web:9a80984d91912c4fefb436"
}

firebase=pyrebase.initialize_app(firebaseConfig)
auth=firebase.auth()

def signup():
    email = input("enter your e-mail: ")
    password = input("enter your password: ")
    user=auth.create_user_with_email_and_password(email,password)

def signin():
    email = input("Enter your email: ")
    password = input("Enter your password: ")
    user=auth.sign_in_with_email_and_password(email,password)

alreadySigned=input("Already installed cam:[y/n] ")
 
if(alreadySigned=='y'):
    try:
        signin()
        print("Successfully Logged in")
    except:
        print("Invalid email or password")
        sys.exit(0)

elif(alreadySigned=='n'):
    try:
        signup()
        print("Created account successfully")
    except:
        print("Account already exists!!")
        a=input("Want to sign in? [y/n]  ")
        if a=='y':
            try:
                signin()
                print("Successfully Logged in")
            except:
                print("Invalid email or password")
                sys.exit(0)

else:
    print("Invalid choice")
    sys.exit(0)


def parse_arguments()->argparse.Namespace:
    parser=argparse.ArgumentParser(description="Live cam")
    parser.add_argument(
        "--webcam_resolution",
        default=[1280,770],
        nargs=2,
        type=int
    )
    args=parser.parse_args()
    return args

def main():
    args = parse_arguments()
    frame_width,frame_height=args.webcam_resolution

    cap=cv2.VideoCapture(0)
    cap.set(cv2.CAP_PROP_FRAME_WIDTH,frame_width)
    cap.set(cv2.CAP_PROP_FRAME_HEIGHT,frame_height)

    model=YOLO("yolov8l.pt","v8")

    box_annotator=sv.BoxAnnotator(
        thickness=2,
        text_thickness=2,
        text_scale=1
    )

    while True:
        ret, frame=cap.read()

        result=model(frame)[0]
        detections= sv.Detections.from_yolov8(result)

        labels=[
            f"{model.model.names[class_id]} {confidence:0.2f}"
            for _,
            confidence,
            class_id,
            _ in detections
        ]

        frame=box_annotator.annotate(
            scene=frame,
            detections=detections,
            labels=labels
        )

        db=firebase.database()
        
        for i in result.tojson():
            db.set(i)

        cv2.imshow("yolov8",frame)

        if (cv2.waitKey(30)==27):
            break

        # for i in labels:
        #     data=repr(i)
        #     results={"detect":data}
        #     db.set(results)
        

if __name__ == "__main__":
    main()