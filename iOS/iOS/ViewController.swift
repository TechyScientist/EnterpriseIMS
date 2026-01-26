//
//  ViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-26.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var lbError: UILabel!
    @IBOutlet weak var errorView: UIView!
    @IBOutlet weak var tfUsername: UITextField!
    @IBOutlet weak var tfPassword: UITextField!
    @IBOutlet weak var indicator: UIActivityIndicatorView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    @IBAction func onSignIn() {
        errorView.isHidden = true
        indicator.isHidden = false
        var request = URLRequest(url: URL(string: "https://wildfly.johnnyconsole.com:8443/ims/api/auth/sign-in")!)
        request.httpMethod = "POST"
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.httpBody = "username=\(tfUsername.text ?? "")&password=\(tfPassword.text ?? "")".data(using: .utf8)
        let session = URLSession(configuration: .default)
        session.dataTask(with: request) { [weak self] data, response, error in
            let responseCode = (response as! HTTPURLResponse).statusCode
            
            DispatchQueue.main.async {
                if(responseCode == StatusCode.OK) {
                    //TODO: Interpret user response, implement storyboard segue to dashboard activity, pass user information to next view controller
                    print("Signed In!")
                }
                else {
                    let errorText = switch(responseCode) {
                    case StatusCode.UNAUTHORIZED: "Invalid credentials, please try again."
                        default: "Missing credentials, please try again."
                    }
                    self!.lbError.text = "Error: \(errorText)"
                    print(self!.lbError.text ?? "NOTHING")
                    self!.errorView.sizeToFit()
                    self!.errorView.isHidden = false
                }
                self!.indicator.isHidden = true
            }
        }.resume()
    }


}

