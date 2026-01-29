//
//  ViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-26.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var lbWarning: UILabel!
    @IBOutlet weak var lbError: UILabel!
    @IBOutlet weak var errorView: UIView!
    @IBOutlet weak var tfUsername: UITextField!
    @IBOutlet weak var tfPassword: UITextField!
    @IBOutlet weak var indicator: UIActivityIndicatorView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        let attributedWarning = NSMutableAttributedString(
            string: "Warning",
            attributes: [.font: UIFont.boldSystemFont(ofSize: CGFloat(17))]
        )
        attributedWarning.append(NSAttributedString(
            string: ": Access to IMS iOS is restricted to authorized users only. Please sign in to continue.",
            attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
        lbWarning.attributedText = attributedWarning
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
        session.dataTask(with: request) { [self] data, response, _ in
            let responseCode = (response as! HTTPURLResponse).statusCode
            
            DispatchQueue.main.async {
                if(responseCode == OK) {
                    if let user = try? JSONDecoder().decode(User.self, from: data!) {
                        UserDefaults.standard.set(user.username, forKey: "ims_username")
                        self.tfPassword.text = ""
                        self.performSegue(withIdentifier: "ShowDashboard", sender: user)
                    }
                    else {
                        let attributedError = NSMutableAttributedString(string: "Error", attributes: [.font: UIFont.boldSystemFont(ofSize: CGFloat(17))])
                        attributedError.append(NSAttributedString(string: ": Could not parse user data.", attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
                        self.lbError.attributedText = attributedError
                        self.errorView.sizeToFit()
                        self.errorView.isHidden = false
                    }
                }
                else {
                    let errorText = switch(responseCode) {
                        case UNAUTHORIZED: "Invalid credentials, please try again."
                        case BAD_REQUEST: "Missing credentials, please try again."
                        default: "Unexpected HTTP response code: \(responseCode)."
                    }
                    let attributedError = NSMutableAttributedString(string: "Error", attributes: [.font: UIFont.boldSystemFont(ofSize: CGFloat(17))])
                    attributedError.append(NSAttributedString(string: ": \(errorText)", attributes: [.font: UIFont.systemFont(ofSize: CGFloat(17))]))
                    self.lbError.attributedText = attributedError
                    self.errorView.sizeToFit()
                    self.errorView.isHidden = false
                }
                self.indicator.isHidden = true
            }
        }.resume()
    }
        
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "ShowDashboard") {
            let destinationVC = segue.destination as! DashboardViewController
            destinationVC.with(sender as! User)
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if(textField == tfUsername) {
            tfPassword.becomeFirstResponder()
        }
        else {
            tfPassword.resignFirstResponder()
            onSignIn()
        }
        return true
    }
}

