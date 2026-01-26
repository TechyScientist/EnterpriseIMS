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
    
    private var userData: Data?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        tfUsername.text = UserDefaults.standard.string(forKey: "ims_username") ?? ""
    }
    
    @IBAction func onSignIn() {
        errorView.isHidden = true
        indicator.isHidden = false
        var request = URLRequest(url: URL(string: "https://wildfly.johnnyconsole.com:8443/ims/api/auth/sign-in")!)
        request.httpMethod = "POST"
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.httpBody = "username=\(tfUsername.text ?? "")&password=\(tfPassword.text ?? "")".data(using: .utf8)
        let session = URLSession(configuration: .default)
        session.dataTask(with: request) { [self] data, response, error in
            let responseCode = (response as! HTTPURLResponse).statusCode
            if(responseCode == StatusCode.OK) {
                userData = data
            }
            DispatchQueue.main.async {
                if(responseCode == StatusCode.OK) {
                    if let user = try? JSONDecoder().decode(User.self, from: self.userData!) {
                        UserDefaults.standard.set(user.username, forKey: "ims_username")
                        self.tfPassword.text = ""
                        self.performSegue(withIdentifier: "ShowDashboard", sender: user)
                    }
                    else {
                        self.lbError.text = "Error: Could not parse user data."
                        self.errorView.sizeToFit()
                        self.errorView.isHidden = false
                    }
                }
                else {
                    let errorText = switch(responseCode) {
                        case StatusCode.UNAUTHORIZED: "Invalid credentials, please try again."
                        case StatusCode.BAD_REQUEST: "Missing credentials, please try again."
                        default: "Unexpected HTTP response code: \(responseCode)."
                    }
                    self.lbError.text = "Error: \(errorText)"
                    self.errorView.sizeToFit()
                    self.errorView.isHidden = false
                }
                self.indicator.isHidden = true
            }
        }.resume()
    }
}

